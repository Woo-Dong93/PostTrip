const mongoose = require('mongoose');
require('dotenv').config();

export const connectMongoDB = () => {
  mongoose
    .connect(`${process.env.DB_HOST}:${process.env.DB_PORT}/`, { dbName: 'Journeydex' })
    .then(() => console.log('Successfully connected to MongoDB'))
    .catch((err: any) => console.log(err));
};
