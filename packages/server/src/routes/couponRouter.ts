import express from 'express';
import { deleteUserCoupon, saveCoupon } from '../service';

const router = express.Router();

/**
 *  @swagger
 *  tags:
 *    name: Coupon
 *    description: Coupon API
 */
/**
 * @swagger
 * /coupon:
 *  post:
 *    summary: Save Coupon
 *    tags: [Coupon]
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
 *              missionId:
 *                type: string
 *                example: "missionId"
 *              title:
 *                type: string
 *                example: "title"
 *              description:
 *                type: string
 *                example: "description"
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
 *                  example: "id"
 *                missionId:
 *                  type: string
 *                  example: "missionId"
 *                title:
 *                  type: string
 *                  example: "title"
 *                description:
 *                  type: string
 *                  example: "description"
 */
router.post('/', saveCoupon);

/**
 * @swagger
 * /coupon/user:
 *  delete:
 *    summary: Use Coupon
 *    tags: [Coupon]
 *    requestBody:
 *      required: true
 *      content:
 *        application/json:
 *          schema:
 *            type: object
 *            properties:
 *              userId:
 *                type: string
 *                example: "userId"
 *              id:
 *                type: string
 *                example: "couponId"
 *    responses:
 *      200:
 *        description: 성공
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                userId:
 *                  type: string
 *                  example: "userId"
 *                id:
 *                  type: string
 *                  example: "couponId"
 *                use:
 *                  type: boolean
 *                  example: "true or false"
 */
router.delete('/user', deleteUserCoupon);

export default router;
