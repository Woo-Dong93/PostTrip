import express from 'express';

export const getMessage = async (req: express.Request, res: express.Response) => {
  try {
    res.status(200).json({ message: 'hello' });
  } catch (e: unknown) {
    res.status(500).json(e);
  }
};
