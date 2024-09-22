import express from 'express';
import { Character, Course, ICharacter, IUserCharacter, User, UserCharacter } from '../schema';

export const saveCharacter = async (req: express.Request<any, any, ICharacter>, res: express.Response) => {
  try {
    const { id, title, contentId, courseContentId } = req.body;

    const characterExisted = await Character.findOne({ id });

    if (characterExisted) {
      return res.status(500).json({ message: 'Character has already been added' });
    }

    const courseExisted = await Course.findOne({ contentId: courseContentId });

    if (!courseExisted) {
      return res.status(500).json({ message: 'No courses found' });
    }

    const character = new Character({
      id,
      title,
      courseContentId,
      contentId,
    });

    await character.save();

    res.status(200).json({ id, title, courseContentId, contentId });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const collectCharacter = async (req: express.Request<any, any, IUserCharacter>, res: express.Response) => {
  try {
    const { userId, id } = req.body;

    const userExisted = await User.findOne({ id: userId });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const characterExisted = await Character.findOne({ id });

    if (!characterExisted) {
      return res.status(500).json({ message: 'Character information not found' });
    }

    const userCharacterExisted = await UserCharacter.findOne({ id, userId });

    if (userCharacterExisted) {
      return res.status(500).json({ message: 'The character has already collected' });
    }

    const userCharacter = new UserCharacter({
      id,
      userId,
    });

    await userCharacter.save();

    res.status(200).json({ id, userId });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

export const getAllUserCharacter = async (req: express.Request<{ id: string }, any, any>, res: express.Response) => {
  try {
    const { id } = req.params;

    const userExisted = await User.findOne({ id });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    const characters = (await Character.find()) as ICharacter[];

    const userCharacters = await UserCharacter.find({ userId: id });

    const userCharacterMap = userCharacters.reduce((map, { userId, id }) => {
      return {
        ...map,
        [id]: userId,
      };
    }, {}) as { [key in string]: string };

    const allUserCharacters = characters.map(({ id, title, courseContentId, contentId }) => {
      return {
        id,
        title,
        courseContentId,
        contentId,
        collected: Boolean(userCharacterMap[id]),
      };
    });

    return res.status(200).json({ data: allUserCharacters });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
