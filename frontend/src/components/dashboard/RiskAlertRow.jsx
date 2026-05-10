const severityClasses = {
  CRITICAL: 'bg-red-50 text-red-600 border border-red-200',
  HIGH: 'bg-orange-50 text-orange-600 border border-orange-200',
  MEDIUM: 'bg-yellow-50 text-yellow-600 border border-yellow-200',
  LOW: 'bg-green-50 text-green-600 border border-green-200',
};

function formatDistanceToNow(value) {
  if (!value) return 'just now';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return 'just now';
  const diffMs = Date.now() - date.getTime();
  const diffHours = Math.floor(diffMs / 3_600_000);
  if (diffHours >= 24) return `${Math.floor(diffHours / 24)}d ago`;
  if (diffHours >= 1) return `${diffHours}h ago`;
  const diffMinutes = Math.max(1, Math.floor(diffMs / 60_000));
  return `${diffMinutes}m ago`;
}

export default function RiskAlertRow({ risk }) {
  const severity = String(risk?.severity || 'LOW').toUpperCase();
  const timeAgo = formatDistanceToNow(risk?.detectedAt || risk?.detected);

  return (
    <div className="flex items-start gap-4 py-3.5 border-b border-surface-border last:border-0 hover:bg-surface-muted/50 transition-colors rounded-lg px-2 -mx-2 cursor-pointer">
      <span className={`mt-0.5 px-2 py-0.5 rounded-md text-xs font-bold uppercase tracking-wide shrink-0 ${severityClasses[severity] || severityClasses.LOW}`}>
        {severity}
      </span>
      <div className="flex-1 min-w-0">
        <p className="text-sm font-medium text-gray-800 truncate">{risk?.title}</p>
        <p className="text-xs text-gray-400 mt-0.5">{risk?.source || 'AI scan'}</p>
      </div>
      <span className="text-xs text-gray-300 shrink-0 mt-0.5">{timeAgo}</span>
    </div>
  );
}
