
        
        // Handle preflight request
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) {
        // Not needed
    }

    @Override
    public void destroy() {
        // Not needed
    }
}
