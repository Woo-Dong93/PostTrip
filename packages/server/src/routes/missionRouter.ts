import express from 'express';
import { startUserMission, saveMission, deleteUserMission, getMissionByCourse, getUserMission } from '../service';

const router = express.Router();

/**
 *  @swagger
 *  tags:
 *    name: Mission
 *    description: Mission API
 */
/**
 * @swagger
 * /mission:
 *  post:
 *    summary: Save Mission
 *    tags: [Mission]
 *    requestBody:
 *      required: true
 *      content:
 *        application/json:
 *          schema:
 *            type: object
 *            properties:
 *              id:
 *                type: string
 *                example: "missionId"
 *              contentId:
 *                type: string
 *                example: "contentId"
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
 *                  example: "missionId"
 *                contentId:
 *                  type: string
 *                  example: "contentId"
 *                title:
 *                  type: string
 *                  example: "title"
 *                description:
 *                  type: string
 *                  example: "description"
 */
router.post('/', saveMission);

/**
 * @swagger
 * /mission/user:
 *  post:
 *    summary: Start Mission
 *    tags: [Mission]
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
 *                example: "missionId"
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
 *                  example: "missionId"
 *                status:
 *                  type: string
 *                  example: "ACTIVE or COMPLETED"
 */
router.post('/user', startUserMission);

/**
 * @swagger
 * /mission/user:
 *  delete:
 *    summary: Completed Mission
 *    tags: [Mission]
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
 *                example: "missionId"
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
 *                  example: "missionId"
 *                status:
 *                  type: string
 *                  example: "ACTIVE or COMPLETED"
 */
router.delete('/user', deleteUserMission);

/**
 * @swagger
 * /mission/user/{id}/{contentId}:
 *  get:
 *    summary: Course Mission
 *    tags: [Mission]
 *    parameters:
 *      - in: path
 *        name: id
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
 *                userId:
 *                  type: string
 *                  example: "userId"
 *                id:
 *                  type: string
 *                  example: "missionId"
 *                contentId:
 *                  type: string
 *                  example: "contentId"
 *                title:
 *                  type: string
 *                  example: "title"
 *                description:
 *                  type: string
 *                  example: "description"
 *                status:
 *                  type: string
 *                  example: "PENDING or ACTIVE or COMPLETED"
 */
router.get('/user/:id/:contentId', getMissionByCourse);

/**
 * @swagger
 * /mission/user/{id}:
 *  get:
 *    summary: User Mission
 *    tags: [Mission]
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
 *                userId:
 *                  type: string
 *                  example: "userId"
 *                id:
 *                  type: string
 *                  example: "missionId"
 *                contentId:
 *                  type: string
 *                  example: "contentId"
 *                title:
 *                  type: string
 *                  example: "title"
 *                description:
 *                  type: string
 *                  example: "description"
 *                status:
 *                  type: string
 *                  example: "PENDING or ACTIVE or COMPLETED"
 */
router.get('/user/:id/', getUserMission);

export default router;
