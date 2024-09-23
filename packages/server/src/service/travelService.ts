import express from 'express';
import {
  ICourse,
  User,
  Course,
  ICourseKey,
  Onboarding,
  IOnboarding,
  Favorite,
  Character,
  UserCharacter,
  ICharacter,
} from '../schema';
import { isLimitedServiceError } from '../utils';
const axios = require('axios');

export const travelCourse = async (req: express.Request<{ id: string }, any, any>, res: express.Response) => {
  try {
    const { id: userId } = req.params;
    const userExisted = await User.findOne({ id: userId });

    if (userExisted) {
      const result = await axios.get(
        `${process.env.API_URL}/areaBasedList1?MobileOS=AND&MobileApp=Journeydex&serviceKey=${process.env.API_KEY}&contentTypeId=25&_type=json&numOfRows=1070`,
      );

      if (isLimitedServiceError(result.data)) {
        throw new Error('API request limit exceeded');
      }

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
  req: express.Request<{ userId: string; contentId: string }, any, any>,
  res: express.Response,
) => {
  try {
    const { userId, contentId } = req.params;

    const userExisted = await User.findOne({ id: userId });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const result = await axios.get(
      `${process.env.API_URL}/detailInfo1?MobileOS=AND&MobileApp=PostTrip&serviceKey=${process.env.API_KEY}&contentId=${contentId}&contentTypeId=25&_type=json`,
    );

    if (isLimitedServiceError(result.data)) {
      throw new Error('API request limit exceeded');
    }

    const course = result.data.response.body.items;

    if (!course) {
      return res.status(500).json({ message: 'course information not found' });
    }

    const characters = (await Character.find({ courseContentId: contentId })) as ICharacter[];
    const characterIds = characters.map((info) => info.id);

    const characterMap = characters.reduce((map, { id, title, contentId }) => {
      return {
        ...map,
        [contentId]: {
          id,
          title,
        },
      };
    }, {}) as { [key in string]: { id: string; title: string } };

    const userCharacters = await UserCharacter.find({ userId, id: { $in: characterIds } });

    const userCharacterMap = userCharacters.reduce((map, { userId, id }) => {
      return {
        ...map,
        [id]: userId,
      };
    }, {}) as { [key in string]: string };

    const courseDetailInfo = await Promise.all(
      course.item.map(async (courseInfo: any) => {
        const contentId = courseInfo.subcontentid;

        const info = await axios.get(
          `${process.env.API_URL}/detailCommon1?MobileOS=AND&MobileApp=PostTrip&serviceKey=${process.env.API_KEY}&contentId=${contentId}&defaultYN=Y&overviewYN=Y&mapinfoYN=Y&addrinfoYN=Y&firstImageYN=Y&_type=json`,
        );

        if (isLimitedServiceError(info.data)) {
          throw new Error('API request limit exceeded');
        }

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
          ...(characterMap[contentid] && {
            characterInfo: {
              id: characterMap[contentid].id,
              title: characterMap[contentid].title,
              collected: Boolean(userCharacterMap[characterMap[contentid].id]),
            },
          }),
        };
      }),
    );

    // 코스 상세정보 추가
    const courseResult = await axios.get(
      `${process.env.API_URL}/detailCommon1?MobileOS=AND&MobileApp=PostTrip&serviceKey=${process.env.API_KEY}&contentId=${contentId}&defaultYN=Y&overviewYN=Y&mapinfoYN=Y&addrinfoYN=Y&firstImageYN=Y&areacodeYN=Y&_type=json`,
    );

    if (isLimitedServiceError(courseResult.data)) {
      throw new Error('API request limit exceeded');
    }

    const courseInfo = courseResult.data.response.body.items.item[0];
    const existedCourseKeyword = await Course.findOne({ contentId: courseInfo.contentid });

    const adjustedCourse = {
      firstAddress: courseInfo.addr1,
      secondAddress: courseInfo.addr2,
      areaCode: courseInfo.areacode,
      contentId: courseInfo.contentid,
      firstImage: courseInfo.firstimage,
      secondImage: courseInfo.firstimage2,
      x: courseInfo.mapx,
      y: courseInfo.mapy,
      title: courseInfo.title,
      travelStyleKeyword: existedCourseKeyword?.travelStyleKeyword,
      destinationTypeKeyword: existedCourseKeyword?.destinationTypeKeyword,
      travelTypeKeyword: existedCourseKeyword?.travelTypeKeyword,
    };

    return res.status(200).json({ data: { ...adjustedCourse, courseList: courseDetailInfo.filter(Boolean) } });
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
      return res.status(500).json({ message: 'Onboarding does not exist' });
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

    if (isLimitedServiceError(result.data)) {
      throw new Error('API request limit exceeded');
    }

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
      .filter((course) => course) as ICourse[];

    const contentIds = courseList.map((course) => course.contentId);
    const favorites = await Favorite.find({ id: userId, contentId: { $in: contentIds } });
    const favoriteContentIds = new Set(favorites.map((favorite) => favorite.contentId));

    const courseListByFavorite = courseList.map((course) => ({
      ...course,
      favorite: favoriteContentIds.has(course.contentId),
    }));

    return res.status(200).json({ data: courseListByFavorite });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

const travelStyleKeywords = ['', 'healing', 'culture', 'gourmet', 'activity'];
const destinationTypeKeywords = ['', 'beach', 'mountain', 'city', 'island'];
const travelTypeKeywords = ['', 'solo', 'family', 'couple', 'friends'];

const validateKeywords = (keywords: IOnboarding['keywords']) => {
  const { travelStyleKeyword, destinationTypeKeyword, travelTypeKeyword } = keywords;

  const isValidTravelStyle = travelStyleKeywords.includes(travelStyleKeyword);
  const isValidDestinationType = destinationTypeKeywords.includes(destinationTypeKeyword);
  const isValidTravelType = travelTypeKeywords.includes(travelTypeKeyword);

  return isValidTravelStyle && isValidDestinationType && isValidTravelType;
};

const filterCourses = (courseList: ICourse[], keywords: IOnboarding['keywords']) => {
  return courseList.filter((course) => {
    const isTravelStyleMatch =
      keywords.travelStyleKeyword && course.travelStyleKeyword
        ? course.travelStyleKeyword.includes(keywords.travelStyleKeyword)
        : true;

    const isDestinationTypeMatch =
      keywords.destinationTypeKeyword && course.destinationTypeKeyword
        ? course.destinationTypeKeyword.includes(keywords.destinationTypeKeyword)
        : true;

    const isTravelTypeMatch =
      keywords.travelTypeKeyword && course.travelTypeKeyword
        ? course.travelTypeKeyword.includes(keywords.travelTypeKeyword)
        : true;

    return isTravelStyleMatch && isDestinationTypeMatch && isTravelTypeMatch;
  });
};

export const getTypeBasedCourse = async (
  req: express.Request<
    { id: string },
    any,
    { travelStyleKeyword: string; destinationTypeKeyword: string; travelTypeKeyword: string; area: string }
  >,
  res: express.Response,
) => {
  try {
    const { id: userId } = req.params;
    const { area, travelStyleKeyword, destinationTypeKeyword, travelTypeKeyword } = req.body;
    const userExisted = await User.findOne({ id: userId });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    if (!validateKeywords({ travelStyleKeyword, destinationTypeKeyword, travelTypeKeyword })) {
      return res.status(500).json({ message: 'Invalid travelStyleKeyword' });
    }

    const result = await axios.get(
      `${process.env.API_URL}/areaBasedList1?MobileOS=AND&MobileApp=Journeydex&serviceKey=${process.env.API_KEY}&contentTypeId=25&_type=json&numOfRows=1070&areaCode=${area}`,
    );

    if (isLimitedServiceError(result.data)) {
      throw new Error('API request limit exceeded');
    }

    if (!result.data.response.body.items) {
      return res.status(500).json({ message: 'No data available' });
    }

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
      .filter((course) => course) as ICourse[];

    const filteredCourses = filterCourses(courseList, {
      travelStyleKeyword,
      destinationTypeKeyword,
      travelTypeKeyword,
    });

    return res.status(200).json({ data: filteredCourses });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
