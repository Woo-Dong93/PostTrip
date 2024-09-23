import express from 'express';
import {
  Course,
  Mission,
  IMission,
  IUserMission,
  User,
  UserMission,
  Character,
  ICharacter,
  UserCharacter,
  Coupon,
  UserCoupon,
} from '../schema';

const MISSION_STATE = ['PENDING', 'ACTIVE', 'COMPLETED'];

export const saveMission = async (req: express.Request<any, any, IMission>, res: express.Response) => {
  try {
    const { id, contentId, title, description, collectionCount } = req.body;

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
      collectionCount,
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

    const mission = await Mission.findOne({ id });
    const coupon = await Coupon.findOne({ id: mission?.couponId });

    if (coupon) {
      const userCoupon = new UserCoupon({
        id,
        userId,
        use: false,
      });

      await userCoupon.save();
    }

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

    const missionList = mission.map(({ id, contentId, title, description, collectionCount }) => {
      return {
        id,
        contentId,
        title,
        description,
        collectionCount,
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
    const contentIds = mission.map(({ contentId }) => {
      return contentId;
    });

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

    const missionCharacters = await Promise.all(
      contentIds.map(async (contentId) => {
        const characters = (await Character.find({ courseContentId: contentId })) as ICharacter[];
        const characterIds = characters.map((info) => info.id);

        const userCharacters = await UserCharacter.find({ userId: id, id: { $in: characterIds } });
        const userCharacterMap = userCharacters.reduce((map, { userId, id }) => {
          return {
            ...map,
            [id]: userId,
          };
        }, {}) as { [key in string]: string };

        return {
          [contentId]: characters.map(({ id, title, courseContentId, contentId }) => {
            return {
              id,
              title,
              courseContentId,
              contentId,
              collected: Boolean(userCharacterMap[id]),
            };
          }),
        };
      }),
    );

    const missionCharactersMap = missionCharacters.reduce((acc, object) => {
      const key = Object.keys(object)[0];
      return {
        ...acc,
        [key]: object[key],
      };
    }, {});

    const missionList = mission.map(({ id, contentId, title, description, collectionCount }) => {
      const characterInfo = [...missionCharactersMap[contentId]];

      return {
        id,
        contentId,
        title,
        description,
        collectionCount,
        collectedCount: characterInfo.filter((info) => {
          return info.collected;
        }).length,
        status: Boolean(userMissionMap[id]) ? userMissionMap[id] : MISSION_STATE[0],
        ...(missionCharactersMap[contentId] && {
          characters: [...characterInfo],
        }),
      };
    });

    res.status(200).json({ data: missionList });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
