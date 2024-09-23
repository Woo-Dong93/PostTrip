import express from 'express';
import { Mission, ICoupon, Coupon, IUserCoupon, User, UserCoupon } from '../schema';

export const saveCoupon = async (req: express.Request<any, any, ICoupon>, res: express.Response) => {
  try {
    const { id, missionId, title, description } = req.body;

    const missionExisted = await Mission.findOne({ id: missionId });

    if (!missionExisted) {
      return res.status(500).json({ message: 'No Mission found' });
    }

    const coupon = new Coupon({
      id,
      missionId,
      title,
      description,
    });

    await coupon.save();

    res.status(200).json({ id, missionId, title, description });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const deleteUserCoupon = async (req: express.Request<any, any, IUserCoupon>, res: express.Response) => {
  try {
    const { userId, id } = req.body;

    const userExisted = await User.findOne({ id: userId });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const userCouponExisted = await UserCoupon.findOne({ id, userId });

    if (!userCouponExisted) {
      return res.status(500).json({ message: 'Coupon information not found' });
    }

    if (userCouponExisted.use) {
      return res.status(500).json({ message: 'This coupon has already been used' });
    }

    await userCouponExisted.updateOne({ use: true });

    res.status(200).json({ id, userId, use: true });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
