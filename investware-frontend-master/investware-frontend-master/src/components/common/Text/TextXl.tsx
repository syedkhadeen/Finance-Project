import { StyledTextXL } from '@/styles/commonStyle';
import { SxProps } from '@mui/material';

interface Props {
  text: string;
  sx?: SxProps;
}

const TextXL = ({ text, sx }: Props) => {
  return (
    <StyledTextXL
      sx={{
        ...sx,
      }}
    >
      {text}
    </StyledTextXL>
  );
};

export default TextXL;
