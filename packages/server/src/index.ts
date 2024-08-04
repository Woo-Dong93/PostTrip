import express, { Application } from 'express';
import morgan from 'morgan';
import swaggerJSDoc from 'swagger-jsdoc';
import swaggerUi from 'swagger-ui-express';
import Router from './routes';
import { swaggerOptions } from './swagger';
import { connectMongoDB } from './env';

const PORT = process.env.PORT || 3000;

const app: Application = express();
connectMongoDB();

const swaggerSpec = swaggerJSDoc(swaggerOptions);

app.use(express.json());
app.use(morgan('tiny'));
app.use(express.static('public'));

app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec, { explorer: true }));

app.use(Router);

app.listen(PORT, () => {
  console.log('Server is running on port', PORT);
});
