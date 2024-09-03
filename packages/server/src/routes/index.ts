import express from 'express';
import userRouter from './userRouter';
import travelRouter from './travelRouter';
import favoriteRouter from './favoriteRouter';
import missionRouter from './missionRouter';

const router = express.Router();

router.use('/user', userRouter);
router.use('/travel', travelRouter);
router.use('/favorite', favoriteRouter);
router.use('/mission', missionRouter);

export default router;
