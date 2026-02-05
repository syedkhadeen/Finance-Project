import { StyledTextLg } from '@/styles/commonStyle';
import { SxProps } from '@mui/material';

interface Props {
  text: string;
  sx?: SxProps;
}

const TextLg = ({ text, sx }: Props) => {
  return (
    <StyledTextLg
      sx={{
        ...sx,
      }}
    >
      {text}
    </StyledTextLg>
  );
};

export default TextLg;
