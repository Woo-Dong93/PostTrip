import express from 'express';
import { getMessage } from '../service';

const router = express.Router();

router.get('/message', getMessage);

export default router;
