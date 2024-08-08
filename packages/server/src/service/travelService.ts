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
