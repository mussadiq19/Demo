# SovAI Platform Dashboard - React Version

This is the React version of the SovAI Platform Dashboard, converted from the original HTML implementation.

## Project Structure

```
react-dashboard/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── Dashboard.js           # Main dashboard component with navigation
│   │   └── views/
│   │       ├── DashboardOverview.js    # Dashboard overview view
│   │       ├── RiskScanner.js          # Risk scanner view
│   │       ├── SkillsGap.js            # Skills gap analysis view
│   │       └── Roadmap.js              # Personal roadmap view
│   ├── styles/
│   │   └── global.css             # Global styles converted from original
│   ├── App.js                     # Main App component
│   └── index.js                   # Entry point
├── package.json
└── README.md
```

## Features

### Dashboard Overview
- Live threat monitoring banner
- Risk statistics cards with progress bars
- Recent risk alerts with AI insights
- Skills gap analysis by department

### Risk Scanner
- Tabbed interface for risk categories
- Active risk signals with severity indicators
- AI-powered insights and recommendations
- Acknowledge and action plan functionality

### Skills Gap Analyzer
- Employee skills gap analysis
- Missing skills identification
- Gap scoring and severity levels
- Department filtering

### Personal Roadmap
- AI/ML upskilling roadmap
- Progress tracking
- Step completion status
- Time estimates for each step

## Getting Started

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm start
```

3. Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

## Key Differences from HTML Version

- **Component-based architecture**: Each view is now a separate React component
- **State management**: Navigation and UI state managed with React hooks
- **Dynamic content**: Data can be easily updated and managed through props
- **Better maintainability**: Modular structure makes it easier to update and extend
- **Interactive elements**: All buttons and navigation items are fully functional

## Technologies Used

- React 18.2.0
- CSS with CSS variables for theming
- Modern JavaScript (ES6+)

## Original vs React

The React version maintains the exact same visual design and functionality as the original HTML version while providing a more maintainable and scalable codebase structure.
