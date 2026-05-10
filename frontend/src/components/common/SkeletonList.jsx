export default function SkeletonList({ rows = 4 }) {
  return (
    <div className="animate-pulse space-y-3">
      {[...Array(rows)].map((_, i) => (
        <div key={i} className="flex items-center gap-3">
          <div className="w-16 h-5 bg-gray-100 rounded-md" />
          <div className="flex-1 h-4 bg-gray-100 rounded" />
          <div className="w-12 h-4 bg-gray-100 rounded" />
        </div>
      ))}
    </div>
  );
}
