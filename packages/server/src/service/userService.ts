import express from 'express';
import { IUser, User } from '../schema';

export const login = async (req: express.Request<any, any, IUser>, res: express.Response) => {
  try {
    const { id, nickname, auth_provider } = req.body;

    const userExisted = await User.findOne({ id });

    if (!userExisted) {
      const user = new User({
        id,
        nickname,
        auth_provider,
      });

      user.save();
    }

    res.status(200).json({ id, nickname, onboarding: true });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
