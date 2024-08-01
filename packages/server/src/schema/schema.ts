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
