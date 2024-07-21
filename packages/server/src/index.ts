import express, { Application } from 'express';
import morgan from 'morgan';
import swaggerJSDoc from 'swagger-jsdoc';
import swaggerUi from 'swagger-ui-express';
import Router from './routes';
import { swaggerOptions } from './swagger';

const PORT = process.env.PORT || 8000;

const app: Application = express();

const swaggerSpec = swaggerJSDoc(swaggerOptions);

app.use(express.json());
app.use(morgan('tiny'));
app.use(express.static('public'));

app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec));

app.use(Router);

app.listen(PORT, () => {
  console.log('Server is running on port', PORT);
});
