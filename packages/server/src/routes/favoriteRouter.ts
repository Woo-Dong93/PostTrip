import express from 'express';
import { deleteFavoriteTravelCourse, saveFavoriteTravelCourse } from '../service';

const router = express.Router();

/**
 *  @swagger
 *  tags:
 *    name: Favorite
 *    description: Favorite API.
 */
/**
 * @swagger
 * /favorite:
 *  post:
 *    summary: Save Favorite Travel Course
 *    tags: [Favorite]
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
 *              contentId:
 *                type: string
 *                example: "contentsId"
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
 *                contentId:
 *                  type: string
 *                  example: 'contentsId'
 */
router.post('/', saveFavoriteTravelCourse);

/**
 * @swagger
 * /favorite:
 *  delete:
 *    summary: Delete Favorite Travel Course
 *    tags: [Favorite]
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
 *              contentId:
 *                type: string
 *                example: "contentsId"
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
 *                contentId:
 *                  type: string
 *                  example: 'contentsId'
 */
router.delete('/', deleteFavoriteTravelCourse);

export default router;
