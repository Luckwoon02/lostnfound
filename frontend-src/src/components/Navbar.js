import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  useTheme,
  useMediaQuery,
} from '@mui/material';

const Navbar = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography
          variant="h6"
          component={RouterLink}
          to="/"
          sx={{
            flexGrow: 1,
            textDecoration: 'none',
            color: 'inherit',
          }}
        >
          Lost & Found Portal
        </Typography>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button
            color="inherit"
            component={RouterLink}
            to="/lost-item"
          >
            Report Lost Item
          </Button>
          <Button
            color="inherit"
            component={RouterLink}
            to="/found-item"
          >
            Report Found Item
          </Button>
          <Button
            color="inherit"
            component={RouterLink}
            to="/matches"
          >
            View Matches
          </Button>
          <Button
            color="inherit"
            component={RouterLink}
            to="/login"
          >
            Login
          </Button>
          <Button
            color="inherit"
            component={RouterLink}
            to="/register"
          >
            Register
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
