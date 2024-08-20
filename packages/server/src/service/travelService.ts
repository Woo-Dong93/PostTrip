import express from 'express';
import { ICourse, User, Course, ICourseKey, Onboarding } from '../schema';
const axios = require('axios');

export const travelCourse = async (req: express.Request<{ id: string }, any, any>, res: express.Response) => {
  try {
    const { id: userId } = req.params;
    const userExisted = await User.findOne({ id: userId });

    if (userExisted) {
      const result = await axios.get(
        `${process.env.API_URL}/areaBasedList1?MobileOS=AND&MobileApp=Journeydex&serviceKey=${process.env.API_KEY}&contentTypeId=25&_type=json&numOfRows=1070`,
      );

      const originCourseList: ICourse[] = result.data.response.body.items.item.map((item: any) => {
        return {
          firstAddress: item.addr1,
          secondAddress: item.addr2,
          areaCode: item.areacode,
          contentId: item.contentid,
          firstImage: item.firstimage,
          secondImage: item.firstimage2,
          x: item.mapx,
          y: item.mapy,
          title: item.title,
        };
      });

      const contentIds = originCourseList.map((course) => {
        return course.contentId;
      });

      const existedCourseKeyword: ICourseKey[] = await Course.find({ contentId: { $in: contentIds } });
      const contentIdMap = existedCourseKeyword.reduce<{ [ket in string]: Omit<ICourseKey, 'contentId'> }>(
        (map, courseKeyword) => {
          const { contentId, travelStyleKeyword, destinationTypeKeyword, travelTypeKeyword } = courseKeyword;
          return {
            ...map,
            [contentId]: {
              travelStyleKeyword,
              destinationTypeKeyword,
              travelTypeKeyword,
            },
          };
        },
        {},
      );

      const courseList = originCourseList
        .map((course) => {
          return Boolean(contentIdMap[course.contentId]) ? { ...course, ...contentIdMap[course.contentId] } : false;
        })
        .filter((course) => course);

      return res.status(200).json({ data: courseList });
    } else {
      return res.status(500).json({ message: 'User information not found' });
    }
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const getTravelDetailCourse = async (
  req: express.Request<{ contentId: string }, any, any>,
  res: express.Response,
) => {
  try {
    const { contentId } = req.params;

    const result = await axios.get(
      `${process.env.API_URL}/detailInfo1?MobileOS=AND&MobileApp=PostTrip&serviceKey=${process.env.API_KEY}&contentId=${contentId}&contentTypeId=25&_type=json`,
    );

    const course = result.data.response.body.items;

    if (!course) {
      return res.status(500).json({ message: 'course information not found' });
    }

    const courseDetailInfo = await Promise.all(
      course.item.map(async (info: any) => {
        const contentId = info.subcontentid;

        try {
          const info = await axios.get(
            `${process.env.API_URL}/detailCommon1?MobileOS=AND&MobileApp=PostTrip&serviceKey=${process.env.API_KEY}&contentId=${contentId}&defaultYN=Y&overviewYN=Y&mapinfoYN=Y&addrinfoYN=Y&firstImageYN=Y&_type=json`,
          );

          if (!info.data.response.body.items) {
            return null;
          }

          const { contentid, contenttypeid, title, firstimage, firstimage2, addr1, addr2, mapx, mapy, overview } =
            info.data.response.body.items.item[0];

          return {
            contentId: contentid,
            contentTypeId: contenttypeid,
            title,
            firstAddress: addr1,
            secondAddress: addr2,
            firstImage: firstimage,
            secondImage: firstimage2,
            x: mapx,
            y: mapy,
            overview,
          };
        } catch (error) {
          return null;
        }
      }),
    );

    return res.status(200).json({ data: courseDetailInfo.filter(Boolean) });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const getRecommendedCourse = async (req: express.Request<{ id: string }, any, any>, res: express.Response) => {
  try {
    const { id: userId } = req.params;
    const userExisted = await User.findOne({ id: userId });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const onboarding = await Onboarding.findOne({ id: userId });

    if (!onboarding) {
      return res.status(500).json({ message: 'Onboarding already exists' });
    }

    const {
      keywords: { travelStyleKeyword, destinationTypeKeyword, travelTypeKeyword },
    } = onboarding;

    const query = [];

    // 우선순위 1: 세 가지 키워드 모두 만족하는 데이터
    query.push({
      travelStyleKeyword: { $regex: new RegExp(travelStyleKeyword, 'i') },
      destinationTypeKeyword: { $regex: new RegExp(destinationTypeKeyword, 'i') },
      travelTypeKeyword: { $regex: new RegExp(travelTypeKeyword, 'i') },
    });

    // 우선순위 2: 두 가지 키워드를 만족하는 데이터
    query.push(
      {
        travelStyleKeyword: { $regex: new RegExp(travelStyleKeyword, 'i') },
        destinationTypeKeyword: { $regex: new RegExp(destinationTypeKeyword, 'i') },
      },
      {
        travelStyleKeyword: { $regex: new RegExp(travelStyleKeyword, 'i') },
        travelTypeKeyword: { $regex: new RegExp(travelTypeKeyword, 'i') },
      },
      {
        destinationTypeKeyword: { $regex: new RegExp(destinationTypeKeyword, 'i') },
        travelTypeKeyword: { $regex: new RegExp(travelTypeKeyword, 'i') },
      },
    );

    // 우선순위 3: 한 가지 키워드를 만족하는 데이터
    // query.push(
    //   { travelStyleKeyword: { $regex: new RegExp(travelStyleKeyword, 'i') } },
    //   { destinationTypeKeyword: { $regex: new RegExp(destinationTypeKeyword, 'i') } },
    //   { travelTypeKeyword: { $regex: new RegExp(travelTypeKeyword, 'i') } },
    // );

    const course = await Course.find({ $or: query }).exec();

    const sortedCourse = course.sort((a, b) => {
      const aScore = [
        a.travelStyleKeyword.includes(travelStyleKeyword) ? 1 : 0,
        a.destinationTypeKeyword.includes(destinationTypeKeyword) ? 1 : 0,
        a.travelTypeKeyword.includes(travelTypeKeyword) ? 1 : 0,
      ].reduce((acc, val) => acc + val, 0);

      const bScore = [
        b.travelStyleKeyword.includes(travelStyleKeyword) ? 1 : 0,
        b.destinationTypeKeyword.includes(destinationTypeKeyword) ? 1 : 0,
        b.travelTypeKeyword.includes(travelTypeKeyword) ? 1 : 0,
      ].reduce((acc, val) => acc + val, 0);

      return bScore - aScore;
    });

    const result = await axios.get(
      `${process.env.API_URL}/areaBasedList1?MobileOS=AND&MobileApp=Journeydex&serviceKey=${process.env.API_KEY}&contentTypeId=25&_type=json&numOfRows=1070`,
    );

    const originCourseList: ICourse[] = result.data.response.body.items.item.map((item: any) => {
      return {
        firstAddress: item.addr1,
        secondAddress: item.addr2,
        areaCode: item.areacode,
        contentId: item.contentid,
        firstImage: item.firstimage,
        secondImage: item.firstimage2,
        x: item.mapx,
        y: item.mapy,
        title: item.title,
      };
    });

    const contentIdMap = sortedCourse.reduce<{ [ket in string]: Omit<ICourseKey, 'contentId'> }>(
      (map, courseKeyword) => {
        const { contentId, travelStyleKeyword, destinationTypeKeyword, travelTypeKeyword } = courseKeyword;
        return {
          ...map,
          [contentId]: {
            travelStyleKeyword,
            destinationTypeKeyword,
            travelTypeKeyword,
          },
        };
      },
      {},
    );

    const courseList = originCourseList
      .map((course) => {
        return Boolean(contentIdMap[course.contentId]) ? { ...course, ...contentIdMap[course.contentId] } : false;
      })
      .filter((course) => course);

    return res.status(200).json({ data: courseList });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
