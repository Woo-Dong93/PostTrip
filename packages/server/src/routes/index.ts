import express from 'express';
import userRouter from './userRouter';
import travelRouter from './travelRouter';
import favoriteRouter from './favoriteRouter';

const router = express.Router();

router.use('/user', userRouter);
router.use('/travel', travelRouter);
router.use('/favorite', favoriteRouter);

export default router;
