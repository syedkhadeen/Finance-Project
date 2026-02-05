import { PrismaClient } from '@prisma/client';

const database = {
  instance: new PrismaClient(),
};

export type IDBClient = typeof database;

Object.freeze(database);

export default database;
