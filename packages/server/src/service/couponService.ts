import express from 'express';
import { Mission, ICoupon, Coupon, IUserCoupon, User, UserCoupon } from '../schema';
import { Console } from 'console';

export const saveCoupon = async (req: express.Request<any, any, ICoupon>, res: express.Response) => {
  try {
    const { id, missionId, title, description } = req.body;

    const missionExisted = await Mission.findOne({ id: missionId });

    if (!missionExisted) {
      return res.status(500).json({ message: 'No Mission found' });
    }

    const couponExisted = await Coupon.findOne({ id: id });

    if (couponExisted) {
      return res.status(500).json({ message: 'Coupon has already been added' });
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

export const getUserCoupon = async (req: express.Request<{ id: string }, any, any>, res: express.Response) => {
  try {
    const { id } = req.params;

    const userExisted = await User.findOne({ id });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const userCoupon = await UserCoupon.find({ userId: id });

    const couponList = await Coupon.find({ id: { $in: userCoupon.map(({ id }) => id) } });
    const couponMap = couponList.reduce((map, coupon) => {
      return {
        ...map,
        [coupon.id]: coupon,
      };
    }, {}) as { [key in string]: string };

    const userCouponList = userCoupon.map(({ userId, id, use }) => {
      const { id: couponId, missionId, title, description } = couponMap[id] as unknown as ICoupon;

      return {
        userId,
        use,
        info: {
          id: couponId,
          missionId,
          title,
          description,
        },
      };
    });

    res.status(200).json({ data: userCouponList });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
