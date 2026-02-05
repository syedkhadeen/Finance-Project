import { SxProps, Typography } from '@mui/material';
import { MouseEventHandler } from 'react';

interface Props {
  text: string;
  onClick?: MouseEventHandler<HTMLSpanElement>;
  sx?: SxProps;
}

const TextSm = ({ text, sx, onClick }: Props) => {
  return (
    <Typography
      onClick={onClick}
      sx={{
        ...sx,
      }}
    >
      {text}
    </Typography>
  );
};

export default TextSm;
