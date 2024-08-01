import swaggerJSDoc from 'swagger-jsdoc';

const swaggerDefinition: swaggerJSDoc.SwaggerDefinition = {
  info: {
    title: 'Journeydex API',
    version: '0.0.1',
    description: 'Journeydex API 문서입니다.',
    license: {
      name: 'MIT',
      url: 'https://spdx.org/licenses/MIT.html',
    },
  },
  openapi: '3.1.0',
};

const swaggerOptions = {
  swaggerDefinition,
  apis: ['src/routes/*.ts'],
};

export default swaggerOptions;
