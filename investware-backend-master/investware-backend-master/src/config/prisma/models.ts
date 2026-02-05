import { Prisma } from '@prisma/client';
export const PRISMA_MODEL_EXTENSION = Prisma.defineExtension({
  model: {
    users: {
      softDelete(id: number) {
        const context = Prisma.getExtensionContext(this);
        return (context as any).update({
          where: {
            id: id,
          },
          data: {
            deleted: true,
          },
        });
      },
    },
  },
});
