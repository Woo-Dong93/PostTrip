import express from 'express';
import { IUser, User, IOnboarding, Onboarding } from '../schema';

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

    const onboarding = await Onboarding.findOne({ id });

    res.status(200).json({ id, nickname, onboarding: Boolean(onboarding) });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};

const travelStyleKeywords = ['healing', 'culture', 'gourmet', 'activity'];
const destinationTypeKeywords = ['beach', 'mountain', 'city', 'island'];
const travelTypeKeywords = ['solo', 'family', 'couple', 'friends'];

const validateKeywords = (keywords: IOnboarding['keywords']) => {
  const { travelStyleKeyword, destinationTypeKeyword, travelTypeKeyword } = keywords;

  const isValidTravelStyle = travelStyleKeywords.includes(travelStyleKeyword);
  const isValidDestinationType = destinationTypeKeywords.includes(destinationTypeKeyword);
  const isValidTravelType = travelTypeKeywords.includes(travelTypeKeyword);

  return isValidTravelStyle && isValidDestinationType && isValidTravelType;
};

export const saveOnboarding = async (req: express.Request<any, any, IOnboarding>, res: express.Response) => {
  try {
    const { id, keywords } = req.body;

    const userExisted = await User.findOne({ id });

    if (!userExisted) {
      return res.status(500).json({ message: 'User information not found' });
    }

    if (!validateKeywords(keywords)) {
      return res.status(500).json({ message: 'Invalid travelStyleKeyword' });
    }

    const onboardingExisted = await Onboarding.findOne({ id });

    if (onboardingExisted) {
      return res.status(500).json({ message: 'Onboarding already exists' });
    }

    const onboarding = new Onboarding({
      id,
      keywords,
    });

    onboarding.save();

    res.status(200).json({ id, keywords });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
