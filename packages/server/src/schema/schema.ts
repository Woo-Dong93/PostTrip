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
