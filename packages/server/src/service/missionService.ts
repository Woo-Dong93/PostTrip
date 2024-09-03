import express from 'express';
import { Course, Mission, IMission, IUserMission, User, UserMission } from '../schema';

export const saveMission = async (req: express.Request<any, any, IMission>, res: express.Response) => {
  try {
    const { id, contentId, title, description } = req.body;

    const missionExisted = await Mission.findOne({ id });

    if (missionExisted) {
      return res.status(500).json({ message: 'Mission has already been added' });
    }

    const courseExisted = await Course.findOne({ contentId });

    if (!courseExisted) {
      return res.status(500).json({ message: 'No courses found' });
    }

    const mission = new Mission({
      id,
      contentId,
      title,
      description,
    });

    await mission.save();

    res.status(200).json({ id, contentId, title, description });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const startUserMission = async (req: express.Request<any, any, IUserMission>, res: express.Response) => {
  try {
    const { userId, id } = req.body;

    const userExisted = await User.findOne({ id: userId });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const userMissionExisted = await UserMission.findOne({ id, userId });

    if (userMissionExisted) {
      return res.status(500).json({ message: 'Mission has already been added' });
    }

    const missionExisted = await Mission.findOne({ id });

    if (!missionExisted) {
      return res.status(500).json({ message: 'Mission information not found' });
    }

    const userMission = new UserMission({
      id,
      userId,
    });

    await userMission.save();

    res.status(200).json({ id, userId });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const deleteUserMission = async (req: express.Request<any, any, IUserMission>, res: express.Response) => {
  try {
    const { userId, id } = req.body;

    const userExisted = await User.findOne({ id: userId });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const userMissionExisted = await UserMission.findOne({ id, userId });

    if (!userMissionExisted) {
      return res.status(500).json({ message: 'Mission information not found' });
    }

    await UserMission.deleteOne({ id, userId });

    res.status(200).json({ id, userId });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const getMissionByCourse = async (
  req: express.Request<{ id: string; contentId: string }, any, any>,
  res: express.Response,
) => {
  try {
    const { id, contentId } = req.params;

    const userExisted = await User.findOne({ id });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const courseExisted = await Course.findOne({ contentId });

    if (!courseExisted) {
      return res.status(500).json({ message: 'No courses found' });
    }

    const mission = await Mission.find({ contentId });
    const missionIds = mission.map(({ id }) => {
      return id;
    });

    const userMission = await UserMission.find({ userId: id, id: { $in: missionIds } });
    const userMissionMap = userMission.reduce((map, { userId, id }) => {
      return {
        ...map,
        [id]: userId,
      };
    }, {}) as { [key in string]: string };

    const missionList = mission.map(({ id, contentId, title, description }) => {
      return {
        id,
        contentId,
        title,
        description,
        starting: Boolean(userMissionMap[id]),
      };
    });

    res.status(200).json({ data: missionList });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
