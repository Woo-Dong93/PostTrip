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
};

const swaggerOptions = {
  swaggerDefinition,
  apis: ['../routes/*.ts'],
};

export default swaggerOptions;
