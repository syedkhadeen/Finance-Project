import { StyledTextXs } from '@/styles/commonStyle';
import { SxProps } from '@mui/material';
import { MouseEventHandler } from 'react';

interface Props {
  text: string;
  sx?: SxProps;
  onClick?: MouseEventHandler<HTMLSpanElement>;
  noWrap?: boolean;
}

const TextXs = ({ text, sx, noWrap, onClick }: Props) => {
  return (
    <StyledTextXs
      onClick={onClick}
      noWrap={noWrap}
      sx={{
        ...sx,
      }}
    >
      {text}
    </StyledTextXs>
  );
};

export default TextXs;
