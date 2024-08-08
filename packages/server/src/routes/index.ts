import express from 'express';
import userRouter from './userRouter';
import travelRouter from './travelRouter';

const router = express.Router();

router.use('/user', userRouter);
router.use('/travel', travelRouter);

export default router;
