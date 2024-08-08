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
}
