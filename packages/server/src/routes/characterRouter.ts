import express from 'express';
import { collectCharacter, getAllUserCharacter, saveCharacter } from '../service';

const router = express.Router();

/**
 *  @swagger
 *  tags:
 *    name: Character
 *    description: Character API
 */
/**
 * @swagger
 * /character:
 *  post:
 *    summary: Save Character
 *    tags: [Character]
 *    requestBody:
 *      required: true
 *      content:
 *        application/json:
 *          schema:
 *            type: object
 *            properties:
 *              id:
 *                type: string
 *                example: "characterId"
 *              title:
 *                type: string
 *                example: "title"
 *              courseContentId:
 *                type: string
 *                example: "courseContentId"
 *              contentId:
 *                type: string
 *                example: "contentId"
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
 *                  example: "characterId"
 *                title:
 *                  type: string
 *                  example: "title"
 *                courseContentId:
 *                  type: string
 *                  example: "courseContentId"
 *                contentId:
 *                  type: string
 *                  example: "contentId"
 */
router.post('/', saveCharacter);

/**
 * @swagger
 * /character/user:
 *  post:
 *    summary: Collect Character
 *    tags: [Character]
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
 *                example: "characterId"
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
 *                  example: "characterId"
 */
router.post('/user', collectCharacter);

/**
 * @swagger
 * /character/{id}:
 *  get:
 *    summary: All Character List
 *    tags: [Character]
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
 *                id:
 *                  type: string
 *                  example: "characterId"
 *                title:
 *                  type: string
 *                  example: "title"
 *                courseContentId:
 *                  type: string
 *                  example: "courseContentId"
 *                contentId:
 *                  type: string
 *                  example: "contentId"
 *                collected:
 *                  type: boolean
 *                  example: 'true or false'
 */
router.get('/:id', getAllUserCharacter);

export default router;
