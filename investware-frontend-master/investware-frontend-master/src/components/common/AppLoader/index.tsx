import { Box, CircularProgress, SxProps, Theme } from '@mui/material';
import { CSSProperties, memo } from 'react';

interface Props {
  sx?: SxProps<Theme>;
  size?: number;
  style?: CSSProperties;
  color?: string;
}

const AppLoaderComponent = ({ sx, size = 20, color = '#fff' }: Props) => {
  const boxStyle: SxProps<Theme> = sx
    ? sx
    : {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      };

  return (
    <Box sx={boxStyle}>
      <CircularProgress sx={{ color: color }} size={size} />
    </Box>
  );
};

export const AppLoader = memo(AppLoaderComponent);
