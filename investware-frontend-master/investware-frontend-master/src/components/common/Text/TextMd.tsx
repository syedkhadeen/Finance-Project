import { StyledTextMd } from '@/styles/commonStyle';
import { SxProps } from '@mui/material';

interface Props {
  text: string;
  noWrap?: boolean;
  sx?: SxProps;
}

const TextMd = ({ text, sx, noWrap }: Props) => {
  return (
    <StyledTextMd
      noWrap={noWrap}
      sx={{
        ...sx,
      }}
    >
      {text}
    </StyledTextMd>
  );
};

export default TextMd;
