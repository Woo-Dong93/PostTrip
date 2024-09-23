import express from 'express';
import { getRecommendedCourse, getTravelDetailCourse, getTypeBasedCourse, travelCourse } from '../service';

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
 * /travel/detail/{userId}/{contentId}:
 *  get:
 *    summary: Travel Detail Course
 *    tags: [Travel]
 *    parameters:
 *      - in: path
 *        name: userId
 *        required: true
 *        schema:
 *          type: string
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
 *                courseList:
 *                  type: array
 *                  items:
 *                    type: object
 *                    properties:
 *                      contentId:
 *                          type: string
 *                          example: 'contentId'
 *                      contentTypeId:
 *                          type: string
 *                          example: 'contentId'
 *                      title:
 *                          type: string
 *                          example: 'title'
 *                      firstAddress:
 *                          type: string
 *                          example: 'firstAddress'
 *                      secondAddress:
 *                          type: string
 *                          example: 'secondAddress'
 *                      firstImage:
 *                          type: string
 *                          example: 'firstImage'
 *                      secondImage:
 *                          type: string
 *                          example: 'secondImage'
 *                      x:
 *                          type: string
 *                          example: 'x'
 *                      y:
 *                          type: string
 *                          example: 'y'
 *                      overview:
 *                          type: string
 *                          example: 'overview'
 *                      characterInfo:
 *                          type: object
 *                          properties:
 *                            id:
 *                              type: string
 *                              example: 'characterId'
 *                            title:
 *                              type: string
 *                              example: 'title'
 *                            collected:
 *                              type: boolean
 *                              example: 'true or false'
 */
router.get('/detail/:userId/:contentId', getTravelDetailCourse);

/**
 * @swagger
 * /travel/course/recommended/{id}:
 *  get:
 *    summary: Recommended Course
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
 *                favorite:
 *                  type: boolean
 *                  example: true / false
 */
router.get('/course/recommended/:id', getRecommendedCourse);

/**
 * @swagger
 * /travel/course/type/{id}:
 *  post:
 *    summary: Area or Keyword based Course
 *    tags: [Travel]
 *    parameters:
 *      - in: path
 *        name: id
 *        required: true
 *        schema:
 *          type: string
 *    requestBody:
 *      required: true
 *      content:
 *        application/json:
 *          schema:
 *            type: object
 *            properties:
 *              area:
 *                type: string
 *                example: "area"
 *              travelStyleKeyword:
 *                type: string
 *                example: "travelStyleKeyword"
 *              destinationTypeKeyword:
 *                type: string
 *                example: "destinationTypeKeyword"
 *              travelTypeKeyword:
 *                type: string
 *                example: "travelTypeKeyword"
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
router.post('/course/type/:id', getTypeBasedCourse);

export default router;
