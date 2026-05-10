function getBarColor(gapPercentage) {
  if (gapPercentage >= 70) return 'bg-risk-critical';
  if (gapPercentage >= 50) return 'bg-risk-high';
  if (gapPercentage >= 30) return 'bg-risk-medium';
  return 'bg-risk-low';
}

export default function DepartmentGapRow({ dept }) {
  const barColor = getBarColor(dept?.gapPercentage || 0);

  return (
    <div className="flex items-center gap-4 py-2">
      <span className="w-24 text-sm text-gray-600 shrink-0">{dept?.name}</span>
      <div className="flex-1 bg-gray-100 rounded-full h-2 overflow-hidden">
        <div
          className={`h-full rounded-full transition-all duration-700 ${barColor}`}
          style={{ width: `${dept?.gapPercentage || 0}%` }}
        />
      </div>
      <span className="text-sm font-semibold text-gray-700 w-10 text-right">
        {dept?.gapPercentage || 0}%
      </span>
      <span className="text-xs text-gray-400 w-16 text-right">
        {dept?.staffCount || 0} staff
      </span>
    </div>
  );
}
