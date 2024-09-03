import express from 'express';
import { deleteFavoriteTravelCourse, getFavoriteTravelCourse, saveFavoriteTravelCourse } from '../service';

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

/**
 * @swagger
 * /favorite/{id}:
 *  get:
 *    summary: Save Favorite Travel Course
 *    tags: [Favorite]
 *    parameters:
 *      - in: path
 *        name: id
 *        required: true
 *        schema:
 *          type: string
 *    responses:
 *      200:
 *        description: 성공
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                firstAddress:
 *                  type: string
 *                  example: 'firstAddress'
 *                secondAddress:
 *                  type: string
 *                  example: 'secondAddress'
 *                areaCode:
 *                  type: string
 *                  example: 'areaCode'
 *                contentId:
 *                  type: string
 *                  example: 'contentId'
 *                firstImage:
 *                  type: string
 *                  example: 'firstImage'
 *                secondImage:
 *                  type: string
 *                  example: 'secondImage'
 *                x:
 *                  type: string
 *                  example: 'x'
 *                y:
 *                  type: string
 *                  example: 'y'
 *                title:
 *                  type: string
 *                  example: 'title'
 *                travelStyleKeyword:
 *                  type: string
 *                  example: 'travelStyleKeyword'
 *                destinationTypeKeyword:
 *                  type: string
 *                  example: 'destinationTypeKeyword'
 *                travelTypeKeyword:
 *                  type: string
 *                  example: 'travelTypeKeyword'
 */
router.get('/:id', getFavoriteTravelCourse);

export default router;
