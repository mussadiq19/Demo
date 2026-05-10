export default function DashboardSkeleton() {
  return (
    <div className="animate-pulse space-y-6 p-8">
      <div className="grid grid-cols-4 gap-4">
        {[...Array(4)].map((_, i) => (
          <div key={i} className="bg-gray-100 rounded-2xl h-32" />
        ))}
      </div>
      <div className="grid grid-cols-2 gap-6">
        <div className="bg-gray-100 rounded-2xl h-64" />
        <div className="bg-gray-100 rounded-2xl h-64" />
      </div>
    </div>
  );
}
