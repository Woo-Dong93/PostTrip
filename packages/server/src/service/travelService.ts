import express from 'express';
import { ICourse, User, Course, ICourseKey } from '../schema';
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
