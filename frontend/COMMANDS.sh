#!/bin/bash

# Assessment Service Frontend - Quick Command Reference

# 🚀 DEVELOPMENT
echo "=== DEVELOPMENT COMMANDS ==="
echo "npm run dev              # Start dev server (http://localhost:5173)"
echo "npm run build            # Create production build"
echo "npm run preview          # Preview production build locally"
echo "npm run lint             # Lint code"

# 📦 INSTALLATION
echo ""
echo "=== SETUP ==="
echo "npm install              # Install dependencies"
echo "echo 'VITE_API_URL=http://localhost:8087' > .env  # Create .env"

# 🔨 MAINTENANCE
echo ""
echo "=== MAINTENANCE ==="
echo "rm -rf node_modules      # Clear dependencies"
echo "npm cache clean --force  # Clear npm cache"
echo "npm update               # Update packages"

# 🚀 DEPLOYMENT
echo ""
echo "=== DEPLOYMENT ==="
echo "npm run build            # Build for production"
echo "docker build -t assessment-frontend . # Build Docker image"
echo "npm run build -- --outDir dist       # Build to dist/"

# 🐛 DEBUGGING
echo ""
echo "=== DEBUGGING ==="
echo "npm run dev -- --open    # Dev server + auto open browser"
echo "npm run build -- --sourcemap  # Build with source maps"

# 📊 ANALYSIS
echo ""
echo "=== ANALYSIS ==="
echo "npm run lint             # Check code quality"
echo "du -sh dist/             # Check build size"

echo ""
echo "For more info, see:"
echo "  - README.md    (Full documentation)"
echo "  - SETUP.md     (Quick start)"
echo "  - FEATURES.md  (Feature details)"
echo "  - DEPLOYMENT.md (Deploy options)"
