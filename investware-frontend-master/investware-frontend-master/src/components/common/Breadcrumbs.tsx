import React from 'react';
import { Breadcrumbs as MuiBreadcrumbs, Link, Typography } from '@mui/material';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import { useRouter } from 'next/router';

interface BreadcrumbItem {
  label: string;
  href?: string;
}

interface BreadcrumbsProps {
  items: BreadcrumbItem[];
}

export const Breadcrumbs: React.FC<BreadcrumbsProps> = ({ items }) => {
  const router = useRouter();

  const handleClick = (event: React.MouseEvent<HTMLAnchorElement>, href?: string) => {
    if (href) {
      event.preventDefault();
      router.push(href);
    }
  };

  return (
    <MuiBreadcrumbs
      separator={<NavigateNextIcon fontSize="small" />}
      aria-label="breadcrumb"
      sx={{ mb: 2 }}
    >
      {items.map((item, index) => {
        const isLast = index === items.length - 1;
        
        if (isLast || !item.href) {
          return (
            <Typography key={index} color="text.primary" fontSize="0.875rem">
              {item.label}
            </Typography>
          );
        }

        return (
          <Link
            key={index}
            color="inherit"
            href={item.href}
            onClick={(e) => handleClick(e, item.href)}
            underline="hover"
            sx={{
              fontSize: '0.875rem',
              cursor: 'pointer',
              '&:hover': {
                color: 'primary.main',
              },
            }}
          >
            {item.label}
          </Link>
        );
      })}
    </MuiBreadcrumbs>
  );
};
