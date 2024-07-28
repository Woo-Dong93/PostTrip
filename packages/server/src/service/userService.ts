import express from 'express';

export const login = async (req: express.Request, res: express.Response) => {
  try {
    console.log(req.body);
    res.status(200).json({ ...req.body });
  } catch (error: any) {
    console.log(error);
    res.status(500).json({ message: error.message });
  }
};
