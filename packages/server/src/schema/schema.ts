import * as mongoose from 'mongoose';

const Schema = mongoose.Schema;

export interface IUser {
  id: string;
  nickname: string;
  auth_provider: string;
}

const userSchema = new Schema<IUser>({
  id: String,
  nickname: String,
  auth_provider: String,
});

export const User = mongoose.model('User', userSchema);

export interface ICourseKey {
  contentId: string;
  travelStyleKeyword: string;
  destinationTypeKeyword: string;
  travelTypeKeyword: string;
}

const courseSchema = new Schema<ICourseKey>({
  contentId: String,
  travelStyleKeyword: String,
  destinationTypeKeyword: String,
  travelTypeKeyword: String,
});

export const Course = mongoose.model('Course', courseSchema);

export interface ICourse {
  firstAddress: string;
  secondAddress: string;
  areaCode: string;
  contentId: string;
  firstImage: string;
  secondImage: string;
  x: string;
  y: string;
  title: string;
  travelStyleKeyword?: string;
  destinationTypeKeyword?: string;
  travelTypeKeyword?: string;
  favorite?: boolean;
  characterInfo?: {
    id: string;
    title: string;
    collected: boolean;
  };
}

export interface IOnboarding {
  id: string;
  keywords: {
    travelStyleKeyword: string;
    destinationTypeKeyword: string;
    travelTypeKeyword: string;
  };
}

const onboardingSchema = new Schema<IOnboarding>({
  id: String,
  keywords: {
    travelStyleKeyword: String,
    destinationTypeKeyword: String,
    travelTypeKeyword: String,
  },
});

export const Onboarding = mongoose.model('Onboarding', onboardingSchema);

export interface IFavorite {
  id: string;
  contentId: string;
}

const favoriteSchema = new Schema<IFavorite>({
  id: String,
  contentId: String,
});

export const Favorite = mongoose.model('Favorite', favoriteSchema);

export interface IMission {
  id: string;
  contentId: string;
  title: string;
  description: string;
}

const missionSchema = new Schema<IMission>({
  id: String,
  contentId: String,
  title: String,
  description: String,
});

export const Mission = mongoose.model('Mission', missionSchema);

export interface IUserMission {
  userId: string;
  id: string;
  status: 'ACTIVE' | 'COMPLETED';
}

const userMissionSchema = new Schema<IUserMission>({
  userId: String,
  id: String,
  status: String,
});

export const UserMission = mongoose.model('UserMission', userMissionSchema);

export interface ICharacter {
  id: string;
  title: string;
  courseContentId: string;
  contentId: string;
}

const characterSchema = new Schema<ICharacter>({
  id: String,
  title: String,
  courseContentId: String,
  contentId: String,
});

export const Character = mongoose.model('Character', characterSchema);

export interface IUserCharacter {
  userId: string;
  id: string;
}

const userCharacterSchema = new Schema<IUserCharacter>({
  userId: String,
  id: String,
});

export const UserCharacter = mongoose.model('UserCharacter', userCharacterSchema);
