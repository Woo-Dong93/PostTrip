import express from 'express';
import { Course, Mission, IMission, IUserMission, User, UserMission } from '../schema';

const MISSION_STATE = ['PENDING', 'ACTIVE', 'COMPLETED'];

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

    const missionExisted = await Mission.findOne({ id });

    if (!missionExisted) {
      return res.status(500).json({ message: 'Mission information not found' });
    }

    if (userMissionExisted && userMissionExisted.status === 'ACTIVE') {
      return res.status(500).json({ message: 'The mission has already started' });
    }

    if (userMissionExisted && userMissionExisted.status === 'COMPLETED') {
      return res.status(500).json({ message: 'The mission has already been completed.' });
    }

    const userMission = new UserMission({
      id,
      userId,
      status: MISSION_STATE[1],
    });

    await userMission.save();

    res.status(200).json({ id, userId, status: MISSION_STATE[1] });
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

    if (userMissionExisted.status === 'COMPLETED') {
      return res.status(500).json({ message: 'The mission has already been completed.' });
    }

    await userMissionExisted.updateOne({ status: MISSION_STATE[2] });

    res.status(200).json({ id, userId, status: MISSION_STATE[2] });
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
    const userMissionMap = userMission.reduce((map, { status, id }) => {
      return {
        ...map,
        [id]: status,
      };
    }, {}) as { [key in string]: string };

    const missionList = mission.map(({ id, contentId, title, description }) => {
      return {
        id,
        contentId,
        title,
        description,
        status: Boolean(userMissionMap[id]) ? userMissionMap[id] : MISSION_STATE[0],
      };
    });

    res.status(200).json({ data: missionList });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const getUserMission = async (
  req: express.Request<{ id: string; contentId: string }, any, any>,
  res: express.Response,
) => {
  try {
    const { id } = req.params;

    const userExisted = await User.findOne({ id });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const mission = await Mission.find();

    const missionIds = mission.map(({ id }) => {
      return id;
    });

    const userMission = await UserMission.find({ userId: id, id: { $in: missionIds } });
    const userMissionMap = userMission.reduce((map, { status, id }) => {
      return {
        ...map,
        [id]: status,
      };
    }, {}) as { [key in string]: string };

    const missionList = mission.map(({ id, contentId, title, description }) => {
      return {
        id,
        contentId,
        title,
        description,
        status: Boolean(userMissionMap[id]) ? userMissionMap[id] : MISSION_STATE[0],
      };
    });

    res.status(200).json({ data: missionList });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
