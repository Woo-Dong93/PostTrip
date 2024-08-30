import express from 'express';
import { User, Course, Favorite, IFavorite } from '../schema';

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
