import express from 'express';
import userRouter from './userRouter';
import travelRouter from './travelRouter';
import favoriteRouter from './favoriteRouter';
import missionRouter from './missionRouter';
import characterRouter from './characterRouter';

const router = express.Router();

router.use('/user', userRouter);
router.use('/travel', travelRouter);
router.use('/favorite', favoriteRouter);
router.use('/mission', missionRouter);
router.use('/character', characterRouter);

export default router;
