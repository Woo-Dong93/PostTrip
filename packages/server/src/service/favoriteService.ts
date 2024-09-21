import express from 'express';
const axios = require('axios');
import { User, Course, Favorite, IFavorite, ICourseKey, ICourse } from '../schema';
import { isLimitedServiceError } from '../utils';

export const saveFavoriteTravelCourse = async (req: express.Request<any, any, IFavorite>, res: express.Response) => {
  try {
    const { id, contentId } = req.body;

    const userExisted = await User.findOne({ id });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const favoriteExisted = await Favorite.findOne({ id, contentId });

    if (favoriteExisted) {
      return res.status(500).json({ message: 'Favorite has already been added' });
    }

    const courseExisted = await Course.findOne({ contentId });

    if (!courseExisted) {
      return res.status(500).json({ message: 'No courses found' });
    }

    const favorite = new Favorite({
      id,
      contentId,
    });

    await favorite.save();

    res.status(200).json({ id, contentId });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const deleteFavoriteTravelCourse = async (req: express.Request<any, any, IFavorite>, res: express.Response) => {
  try {
    const { id, contentId } = req.body;

    const userExisted = await User.findOne({ id });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const favoriteExisted = await Favorite.findOne({ id, contentId });

    if (!favoriteExisted) {
      return res.status(500).json({ message: 'Favorite not found' });
    }

    await Favorite.deleteOne({ id, contentId });

    res.status(200).json({ id, contentId });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const getFavoriteTravelCourse = async (
  req: express.Request<{ id: string }, any, any>,
  res: express.Response,
) => {
  try {
    const { id } = req.params;

    const userExisted = await User.findOne({ id });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const favorites = await Favorite.find({ id: id });

    const contentIds = favorites.map(({ contentId }) => {
      return contentId;
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

    const courseKeywords: ICourseKey[] = await Course.find({ contentId: { $in: contentIds } });
    const contentIdMap = courseKeywords.reduce<{ [ket in string]: Omit<ICourseKey, 'contentId'> }>(
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

    res.status(200).json({ data: courseList });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
