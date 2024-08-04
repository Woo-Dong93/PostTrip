import express from 'express';
import { login } from '../service';

const router = express.Router();

/**
 *  @swagger
 *  tags:
 *    name: User
 *    description: User API.
 */
/**
 * @swagger
 * /user/login:
 *  post:
 *    summary: User Login
 *    tags: [User]
 *    requestBody:
 *      required: true
 *      content:
 *        application/json:
 *          schema:
 *            type: object
 *            properties:
 *              id:
 *                type: string
 *                example: "id"
 *              nickname:
 *                type: string
 *                example: "nickname"
 *              auth_provider:
 *                type: string
 *                example: "kakao"
 *    responses:
 *      200:
 *        description: 성공
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                id:
 *                  type: string
 *                  example: 'id'
 *                nickname:
 *                  type: string
 *                  example: "nickname"
 *                onboarding:
 *                  type: boolean
 *                  example: true
 */
router.post('/login', login);

export default router;
