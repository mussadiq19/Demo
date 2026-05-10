module.exports = {
  content: ["./index.html","./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        brand: { 50:'#eef2ff',100:'#e0e7ff',500:'#6366f1',600:'#4f46e5',700:'#4338ca',900:'#1e1b4b' },
        risk: { low:'#22c55e',medium:'#f59e0b',high:'#f97316',critical:'#ef4444' },
        surface: { DEFAULT:'#ffffff',muted:'#f8fafc',border:'#e2e8f0' },
      },
      fontFamily: { sans: ['Inter','system-ui','sans-serif'] },
      borderRadius: { xl:'1rem','2xl':'1.5rem' },
      boxShadow: { card:'0 1px 3px 0 rgb(0 0 0/0.08)','card-hover':'0 4px 12px 0 rgb(0 0 0/0.1)' },
    },
  },
  plugins: [],
}
