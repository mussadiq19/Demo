function getColorClasses(color) {
  switch (color) {
    case 'red':
      return { value: 'text-red-600', bar: 'bg-risk-critical' };
    case 'orange':
      return { value: 'text-orange-600', bar: 'bg-risk-high' };
    case 'blue':
      return { value: 'text-brand-600', bar: 'bg-brand-500' };
    case 'green':
      return { value: 'text-green-600', bar: 'bg-risk-low' };
    default:
      return { value: 'text-gray-800', bar: 'bg-brand-500' };
  }
}

export default function MetricCard({ label, value, trend, color = 'blue' }) {
  const classes = getColorClasses(color);
  const numericValue = typeof value === 'number'
    ? value
    : parseInt(String(value).replace(/[^0-9]/g, ''), 10) || 0;
  const trendUp = !String(trend || '').includes('↓');

  return (
    <div className="bg-white rounded-2xl p-6 shadow-card hover:shadow-card-hover transition-shadow border border-surface-border">
      <p className="text-xs font-semibold uppercase tracking-widest text-gray-400 mb-3">{label}</p>
      <div className="flex items-end justify-between">
        <span className={`text-4xl font-bold ${classes.value}`}>{value}</span>
        <div className="w-16 h-1.5 rounded-full bg-gray-100 overflow-hidden">
          <div
            className={`h-full rounded-full ${classes.bar}`}
            style={{ width: `${Math.max(12, Math.min(100, numericValue))}%` }}
          />
        </div>
      </div>
      <p className="text-xs text-gray-400 mt-2 flex items-center gap-1">
        <span className={trendUp ? 'text-risk-critical' : 'text-risk-low'}>
          {trendUp ? '↑' : '↓'}
        </span>
        {trend}
      </p>
    </div>
  );
}
