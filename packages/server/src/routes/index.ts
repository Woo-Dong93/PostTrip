import express from 'express';
import PingRouter from './pingRouter';

const router = express.Router();

router.use('/ping', PingRouter);

export default router;
