import express from 'express';
import { getTravelDetailCourse, travelCourse } from '../service';

const router = express.Router();

/**
 *  @swagger
 *  tags:
 *    name: Travel
 *    description: Travel API.
 */

/**
 * @swagger
 * /travel/course/{id}:
 *  get:
 *    summary: Travel Course
 *    tags: [Travel]
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
router.get('/course/:id', travelCourse);

/**
 * @swagger
 * /travel/detail/{contentId}:
 *  get:
 *    summary: Travel Course
 *    tags: [Travel]
 *    parameters:
 *      - in: path
 *        name: contentId
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
 *                contentId:
 *                  type: string
 *                  example: 'contentId'
 *                contentTypeId:
 *                  type: string
 *                  example: 'contentId'
 *                title:
 *                  type: string
 *                  example: 'title'
 *                firstAddress:
 *                  type: string
 *                  example: 'firstAddress'
 *                secondAddress:
 *                  type: string
 *                  example: 'secondAddress'
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
 *                overview:
 *                  type: string
 *                  example: 'overview'
 */
router.get('/detail/:contentId', getTravelDetailCourse);

export default router;
